import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApp } from '../app.model';
import { AppService } from '../service/app.service';

const appResolve = (route: ActivatedRouteSnapshot): Observable<null | IApp> => {
  const id = route.params['id'];
  if (id) {
    return inject(AppService)
      .find(id)
      .pipe(
        mergeMap((app: HttpResponse<IApp>) => {
          if (app.body) {
            return of(app.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default appResolve;
