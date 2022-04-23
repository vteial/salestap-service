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

const homeView = {
    name: 'Home',
    template: '#app-home-view'
};

const registerOwnerView = {
    name: 'Register Owner',
    data() {
        var o = {
            firstName: 'Eialarasu',
            lastName: 'VT',
            email: 'vteial@gmail.com',
            mobileNo: '123456789',
            userId: 'vteial',
            password: '1234'
        }
//        var o = {
//            firstName: null,
//            lastName: null
//            email: null,
//            mobileNo: null,
//            userId: null,
//            password: null
//        }
        return {
            item: o
        }
    },
    methods: {
        save() {
            console.log(this.item);
            http.post("/set-up/register-owner", this.item).then(res => {
                console.log(res);
                if( res.status === 200) {
                   this.items = res.data.map(o => { return Product.from(o); });
                } else {
                   console.log('Fetching products failed...');
                }
            });
        }
    },
    template: '#app-register-owner-view'
};

const createShopView = {
    name: 'Create Shop',
    template: '#app-create-shop-view'
};

const summaryView = {
    name: 'Summary',
    template: '#app-summary-view'
};

const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes: [
        { path: '/:pathMatch(.*)*', name: 'NotFound', component: notFoundView },
        { path: '/', redirect: '/home' },
        { path: '/home', name: homeView.name, component: homeView },
        { path: '/register-owner', name: registerOwnerView.name, component: registerOwnerView},
        { path: '/create-shop', name: createShopView.name, component: createShopView},
        { path: '/summary', name: summaryView.name, component: summaryView},
    ]
});

const app = Vue.createApp({
    name: 'app',
    data() {
        return {
            appName: 'SalesTap'
        }
    },
    template: '#app-root-view'
});

app.use(router);

app.mount("#app");