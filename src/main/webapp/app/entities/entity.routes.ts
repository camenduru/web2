import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'web2App.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'setting',
    data: { pageTitle: 'web2App.setting.home.title' },
    loadChildren: () => import('./setting/setting.routes'),
  },
  {
    path: 'app',
    data: { pageTitle: 'web2App.app.home.title' },
    loadChildren: () => import('./app/app.routes'),
  },
  {
    path: 'job',
    data: { pageTitle: 'web2App.job.home.title' },
    loadChildren: () => import('./job/job.routes'),
  },
  {
    path: 'redeem',
    data: { pageTitle: 'web2App.redeem.home.title' },
    loadChildren: () => import('./redeem/redeem.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
