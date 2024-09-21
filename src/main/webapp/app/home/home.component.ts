import { Component, NgZone, inject, signal, OnInit, OnDestroy } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, Observable, Subscription, tap } from 'rxjs';
import { Subject } from 'rxjs';
import { takeUntil, finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { sortStateSignal, SortDirective, SortByDirective, type SortState, SortService } from 'app/shared/sort';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { SORT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { IJob } from '../entities/job/job.model';
import { EntityArrayResponseType as JobEntityArrayResponseType, JobService } from '../entities/job/service/job.service';
import { IApp } from '../entities/app/app.model';
import { EntityArrayResponseType as AppEntityArrayResponseType, AppService } from '../entities/app/service/app.service';
import { JobFormService, JobFormGroup } from '../entities/job/update/job-form.service';
import { UserService } from '../entities/user/service/user.service';
import { ISchema, TemplateSchemaModule } from 'ngx-schema-form';
import { TrackerService } from 'app/core/tracker/tracker.service';
import { JobStatus } from '../entities/enumerations/job-status.model';
import { JobSource } from '../entities/enumerations/job-source.model';
import dayjs from 'dayjs/esm';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    ItemCountComponent,
    HasAnyAuthorityDirective,
    TemplateSchemaModule,
  ],
  template: '<sf-form [schema]="activeSchema" [model]="activeModel" [actions]="activeActions"></sf-form>',
})
export default class HomeComponent implements OnInit, OnDestroy {
  isInitialLoad = false;
  isSaving = false;
  subscription: Subscription | null = null;
  account = signal<Account | null>(null);
  user: Account = {} as Account;
  apps?: IApp[];
  jobs?: IJob[];
  categories?: string[];
  filteredApps?: IApp[];
  itemsPerPage = ITEMS_PER_PAGE;
  totalJobItems = 0;
  totalAppItems = 0;
  page = 1;
  isLoading = false;
  sortState = sortStateSignal({});

  isActive = true;
  activeSchema: ISchema = {};
  activeModel: any = {};
  default_category: any = {};
  default_app_type: any = {};

  horizontal = false;
  percentage = false;
  isEqualSize = false;
  isConstantSize = false;
  gap = 5;
  resizeDebounce = 100;
  maxResizeDebounce = 0;
  autoResize = true;
  useFit = true;
  useTransform = false;
  renderOnPropertyChange = true;
  preserveUIOnDestroy = false;
  defaultDirection = 'end' as const;
  outlineLength = 0;
  outlineSize = 0;
  useRoundedSize = true;
  useResizeObserver = true;
  observeChildren = true;
  align = 'center' as const;

  column = 0;
  columnSize = 0;
  columnSizeRatio = 0;
  contentAlign = 'masonry' as const;
  columnCalculationThreshold = 1;
  maxStretchColumnSize = Infinity;

  columnRange = [3, 3];
  rowRange = [1, Infinity];
  sizeRange = [0, Infinity];
  displayedRow = -1;
  isCroppedSize = true;

  aspectRatio = 1;
  sizeWeight = 1;
  ratioWeight = 100;
  weightPriority = 'custom' as const;

  frame = [
    [1, 1, 2, 2],
    [3, 3, 2, 2],
    [4, 4, 4, 5],
  ];
  useFrameFill = false;
  rectSize = 0;

  activeActions = {
    enter: (property: any) => {
      const enterButton = document.querySelector('.llm.btn');
      if (enterButton instanceof HTMLButtonElement) {
        enterButton.disabled = true;
      }
      this.isSaving = true;
      const job = this.jobFormService.getJob(this.editForm);
      const updateJob = (command: string): void => {
        job.command = command;
        job.login = 'login';
        job.date = dayjs();
        job.status = JobStatus.WAITING;
        job.amount = 'amount';
        job.notifyUri = 'notifyUri';
        job.notifyToken = 'notifyToken';
        job.discordUsername = 'discordUsername';
        job.discordChannel = 'discordChannel';
        job.discordId = 'discordId';
        job.discordToken = 'discordToken';
        job.source = JobSource.WEB;
        job.total = 'total';
        job.result = 'result';
        if (job.id === null) {
          this.subscribeToSaveResponse(this.jobService.create(job));
        }
      };
      if (property.value.input_image_check) {
        const img = new Image();
        img.src = property.value.input_image_check;
        img.onload = () => {
          property.value.width = img.width;
          property.value.height = img.height;
          updateJob(JSON.stringify(property.value));
        };
        img.onerror = () => {
          updateJob(JSON.stringify(property.value));
        };
      } else {
        updateJob(JSON.stringify(property.value));
      }
    },
  };

  protected jobFormService = inject(JobFormService);
  protected jobService = inject(JobService);
  protected appService = inject(AppService);
  protected sortService = inject(SortService);
  protected activatedRoute = inject(ActivatedRoute);
  protected ngZone = inject(NgZone);
  protected userService = inject(UserService);
  protected accountService = inject(AccountService);
  protected router = inject(Router);
  protected trackerService = inject(TrackerService);

  private readonly destroy$ = new Subject<void>();

  changeSchema(event: Event): void {
    const selectedValue = (event.target as HTMLInputElement).value;
    if (this.apps && this.apps.length > 0) {
      const app = this.apps.find(item => item.type === selectedValue);
      const jsonSchema = app?.schema ? JSON.parse(app.schema) : null;
      this.activeSchema = jsonSchema as unknown as ISchema;
      const jsonModel = app?.model ? JSON.parse(app.model) : null;
      this.activeModel = jsonModel;
    }
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: JobFormGroup = this.jobFormService.createJobFormGroup();

  trackId = (_index: number, item: IJob): string => this.jobService.getJobIdentifier(item);

  ngOnInit(): void {
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.queryAppBackend().subscribe({
          next: (res: AppEntityArrayResponseType) => {
            this.onAppResponseSuccess(res);
          },
        });

        this.trackerService
          .subscribeToNotify('')
          .pipe(
            tap(() => {
              this.load();
            }),
          )
          .subscribe({
            next(message) {
              if (message.includes('Oops!')) {
                const notify = document.getElementById('notify');
                const notifyDivHTML = `
                    <div id="notifyDiv" class="alert alert-dismissible alert-warning">
                        <button id="notifyButton" type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        ${message}
                    </div>
                `;
                if (notify) {
                  notify.innerHTML = notifyDivHTML;
                  const notifyButton = document.getElementById('notifyButton');
                  if (notifyButton) {
                    notifyButton.addEventListener('click', notifyDivRemove);
                  }
                }
              }
            },
          });

        this.accountService.getAuthenticationState().subscribe({
          next: (user: Account | null) => {
            if (user) {
              this.user = user;
            }
          },
        });

        this.accountService
          .getAuthenticationState()
          .pipe(takeUntil(this.destroy$))
          .subscribe(account => this.account.set(account));
        this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
          .pipe(
            tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
            tap(() => this.load()),
          )
          .subscribe();
      } else {
        this.queryHomeBackend().subscribe({
          next: (res: JobEntityArrayResponseType) => {
            this.onJobResponseSuccess(res);
          },
        });
      }
    });
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  load(): void {
    const enterButton = document.querySelector('.llm.btn');
    if (enterButton instanceof HTMLButtonElement) {
      enterButton.disabled = false;
    }
    this.queryJobBackend().subscribe({
      next: (res: JobEntityArrayResponseType) => {
        this.onJobResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onJobResponseSuccess(response: JobEntityArrayResponseType): void {
    this.fillComponentAttributesFromJobResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromJobResponseBody(response.body);
    this.jobs = dataFromBody;
  }

  protected fillComponentAttributesFromJobResponseBody(data: IJob[] | null): IJob[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromJobResponseHeader(headers: HttpHeaders): void {
    this.totalJobItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryJobBackend(): Observable<JobEntityArrayResponseType> {
    const { page } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.jobService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected queryHomeBackend(): Observable<JobEntityArrayResponseType> {
    const { page } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.jobService.home(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected onAppResponseSuccess(response: AppEntityArrayResponseType): void {
    this.fillComponentAttributesFromAppResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromAppResponseBody(response.body);
    this.apps = dataFromBody.filter(item => item.isActive === true);
    const defaultApp = this.apps.find(item => item.isDefault === true);
    const jsonSchema = defaultApp?.schema ? JSON.parse(defaultApp.schema) : null;
    this.activeSchema = jsonSchema as unknown as ISchema;
    const jsonModel = defaultApp?.model ? JSON.parse(defaultApp.model) : null;
    this.activeModel = jsonModel;
    this.default_app_type = defaultApp?.type;
    const titles = this.apps.map(app => app.title).join('\n');
    this.categories = this.extractCategories(titles);
    this.default_category = this.extractCategories(defaultApp?.title ?? '');
    this.isInitialLoad = true;
    this.onCategoryChange({ target: { value: this.default_category } });
  }

  protected fillComponentAttributesFromAppResponseBody(data: IApp[] | null): IApp[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromAppResponseHeader(headers: HttpHeaders): void {
    this.totalAppItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryAppBackend(): Observable<AppEntityArrayResponseType> {
    const { page } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: 100,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.appService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJob>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.load();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected download(link: string): void {
    const url = `${link}`;
    window.open(url);
  }

  protected homeRegister(link: string): void {
    const url = `${link}`;
    window.open(url, '_self');
  }

  protected getUrlsFromString(str: string): string[] {
    const correctedStr = str.replace(/'/g, '"');
    return JSON.parse(correctedStr) as string[];
  }

  protected extractCategories(titles: string): string[] {
    const correctedTitles = titles.replace(/'/g, '"');
    const titleArray = correctedTitles.split(/\n/);
    const categories = titleArray
      .map(title => {
        const match = title.match(/\[(.*?)\]/);
        return match ? match[1] : null;
      })
      .filter((category): category is string => category !== null);
    return Array.from(new Set(categories));
  }

  protected onCategoryChange(event: any): void {
    const selectedCategory = event.target.value;
    this.filteredApps = (this.apps ?? []).filter(app => app.title?.includes(`[${selectedCategory}]`));
    if (this.isInitialLoad) {
      this.changeSchema({ target: { value: this.default_app_type } } as unknown as Event);
      this.isInitialLoad = false;
    } else {
      if (this.filteredApps.length > 0) {
        this.default_app_type = this.filteredApps[0].type;
        this.changeSchema({ target: { value: this.default_app_type } } as unknown as Event);
      } else {
        this.default_app_type = null;
      }
    }
  }
}

function notifyDivRemove(): void {
  const notifyDiv = document.getElementById('notifyDiv');
  if (notifyDiv) {
    notifyDiv.remove();
  }
}
