import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SettingComponent } from './list/setting.component';
import { SettingDetailComponent } from './detail/setting-detail.component';
import { SettingUpdateComponent } from './update/setting-update.component';
import SettingResolve from './route/setting-routing-resolve.service';

const settingRoute: Routes = [
  {
    path: '',
    component: SettingComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SettingDetailComponent,
    resolve: {
      setting: SettingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SettingUpdateComponent,
    resolve: {
      setting: SettingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SettingUpdateComponent,
    resolve: {
      setting: SettingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default settingRoute;
