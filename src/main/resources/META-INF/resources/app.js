const http = axios.create({
    baseURL: "http://localhost:8080/api",
    headers: {
        "Content-type": "application/json",
    },
});

const notFoundView = {
    name: 'Not Found',
    template: '<p>Page not found...</p>'
};

const routeGuard = function(to, from) {
    // console.log('isA : ' + setUpService.setUpInfo.isAuthenticated);
    return setUpService.setUpInfo.isAuthenticated ? true : '/home';
};

const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes: [
        { path: '/:pathMatch(.*)*', name: 'NotFound', component: notFoundView },
        { path: '/', redirect: '/home' },
        { path: '/home', name: homeView.name, component: homeView },
        {
            path: '/register-owner',
            name: registerOwnerView.name,
            component: registerOwnerView,
            beforeEnter: routeGuard
        },
        {
            path: '/create-shop',
            name: createShopView.name,
            component: createShopView,
            beforeEnter: routeGuard
        },
        {
            path: '/summary',
            name: summaryView.name,
            component: summaryView,
            beforeEnter: routeGuard
        },
    ]
});

const app = Vue.createApp({
    name: 'app',
    template: '#app-root-view',
    data() {
        return {
            appName: 'SalesTap',
            setUpInfo: setUpService.setUpInfo,
        }
    },
    mounted() {
        setUpService.getSetUpInfo().then(res => {
            console.log(res.data);
            setUpService.setSetUpInfo(res.data);
            this.setUpInfo = setUpService.setUpInfo;
            console.log(this.setUpInfo);
            this.$forceUpdate();
        });
    }
});

app.use(router);

app.mount('#app');