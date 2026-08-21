import { Routes } from '@angular/router';

import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { Products } from './pages/products/products';
import { Categories } from './pages/categories/categories';
import { Suppliers } from './pages/supplier/supplier';
import { InventoryPage } from './pages/inventory/inventory';
import { Transactions } from './pages/transactions/transactions';
import { Analytics } from './pages/analytics/analytics';
import { AiAssistant } from './pages/ai-assistant/ai-assistant';
import { Users } from './pages/users/users';

import { AppLayout } from './layout/app-layout/app-layout';

import { authGuard } from './core/guards/auth-guard';
import { ownerGuard } from './core/guards/owner.guard';
import { managerGuard } from './core/guards/manager.guard';

export const routes: Routes = [

    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },

    {
        path: 'login',
        component: Login
    },

    {
        path: '',
        component: AppLayout,
        canActivate: [authGuard],

        children: [

            {
                path: 'dashboard',
                component: Dashboard
            },

            {
                path: 'products',
                component: Products
            },

            {
                path: 'categories',
                component: Categories
            },

            {
                path: 'suppliers',
                component: Suppliers
            },

            {
                path: 'inventory',
                component: InventoryPage
            },

            {
                path: 'transactions',
                component: Transactions
            },

            {
                path: 'analytics',
                component: Analytics,
                canActivate: [managerGuard]
            },

            {
                path: 'users',
                component: Users,
                canActivate: [ownerGuard]
            },

            {
                path: 'ai-assistant',
                component: AiAssistant,
                canActivate: [managerGuard]
            }

        ]
    },

    {
        path: '**',
        redirectTo: 'login'
    }

];