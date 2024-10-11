import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AppComponent } from './list/app.component';
import { AppDetailComponent } from './detail/app-detail.component';
import { AppUpdateComponent } from './update/app-update.component';
import AppResolve from './route/app-routing-resolve.service';

const appRoute: Routes = [
  {
    path: '',
    component: AppComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppDetailComponent,
    resolve: {
      app: AppResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id',
    component: AppDetailComponent,
    resolve: {
      app: AppResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppUpdateComponent,
    resolve: {
      app: AppResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppUpdateComponent,
    resolve: {
      app: AppResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default appRoute;
