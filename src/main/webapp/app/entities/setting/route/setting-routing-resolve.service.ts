import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISetting } from '../setting.model';
import { SettingService } from '../service/setting.service';

const settingResolve = (route: ActivatedRouteSnapshot): Observable<null | ISetting> => {
  const id = route.params['id'];
  if (id) {
    return inject(SettingService)
      .find(id)
      .pipe(
        mergeMap((setting: HttpResponse<ISetting>) => {
          if (setting.body) {
            return of(setting.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default settingResolve;
