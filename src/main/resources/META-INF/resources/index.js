const http = axios.create({
    baseURL: "http://localhost:8080/api",
    headers: {
        "Content-type": "application/json",
    },
});

const setUpService = {
    sadminInfo: { userId: 'sadmin', password: null },
    setUpInfo: {
        state: 'New',
        steps: { 'register-owner': false, 'create-shop': false, 'summary': false },
        termsAndConditions: false
   },
   ownerInfo: {
       id: 0,
       firstName: null,
       lastName: null,
       emailId: null,
       mobileNo: null,
       userId: null,
       password: null
    },
   shopInfo: {
        id: 0,
        code: null,
        name: null,
        aliasName: null,
   },
   getSetUpInfo: function() {
       return http.get('/set-up/current-state');
   },
   setSetUpInfo: function(item) {
       this.setUpInfo.state = item.state;
       this.setUpInfo.steps = item.steps;
       this.setUpInfo.termsAndConditions = item.termsAndConditions;
   },
   auth: function(item) {
       return http.post('/set-up/auth', item);
   },
   registerOwner: function(item) {
       return http.post('/set-up/register-owner', item);
   },
   createShop: function(item) {
       return http.post('/set-up/create-shop', item);
   },
   acceptTermsAndConditions: function(atc) {
       return http.post('/set-up/accept-terms-and-conditions?atc='+atc);
   }
}

const notFoundView = {
    name: 'Not Found',
    template: '<p>Page not found...</p>'
};

const homeView = {
    name: 'Home',
    template: '#app-home-view',
    mounted() {
        console.log('home mounted...');
        console.log(setUpService.sadminInfo);
    },
    data() {
        var o = {
            userId: setUpService.sadminInfo.userId,
            password: setUpService.sadminInfo.password
        };
        o.password = 'salestap4321'
        return {
            item: o,
            message: null
        }
    },
    methods: {
        auth() {
            this.message = null;
            // console.log(this.item);
            setUpService.auth(this.item).then(res => {
                console.log(res);
                if(res.data.type === 0)
                    this.$router.push('/register-owner');
                else
                    this.message = res.data.message;
            });
        }
    }
};

const registerOwnerView = {
    name: 'Register Owner',
    template: '#app-register-owner-view',
    mounted() {
        console.log('register-owner mounted');
        console.log(setUpService.ownerInfo);
    },
    data() {
        var o = setUpService.ownerInfo;
        // console.log(o);
        if(o.id === 0) {
            o = {
                firstName: 'Eialarasu',
                lastName: 'VT',
                emailId: 'vteial@gmail.com',
                mobileNo: '123456789',
                userId: 'vteial',
                password: '1234'
            }
        }
        return {
            item: o,
            message: null
        }
    },
    methods: {
        proceed() {
            this.message = null;
            this.item.token = this.item.password;
            // console.log(this.item);
            setUpService.registerOwner(this.item).then(res => {
                console.log(res);
                if(res.data.type === 0) {
                    setUpService.ownerInfo = res.data.data.ownerInfo
                    setUpService.setSetUpInfo(res.data.data.setUpInfo);
                    this.$router.push('/create-shop');
                } else {
                    this.message = res.data.message;
                }
            });
        }
    }
};

const createShopView = {
    name: 'Create Shop',
    template: '#app-create-shop-view',
    mounted() {
        console.log('create-shop mounted');
        console.log(setUpService.ownerInfo);
    },
    data() {
        var o = setUpService.shopInfo;
        // console.log(o);
        if(o.id === 0) {
            o = {
                id: 0,
                code: 'shop-ho',
                name: 'National Super Market',
                aliasName: 'Main Branch',
            }
        }
        return {
            item: o,
            message: null
        }
    },
    methods: {
        proceed() {
            this.message = null;
            // console.log(this.item);
            setUpService.createShop(this.item).then(res => {
                console.log(res);
                if(res.data.type === 0) {
                    setUpService.shopInfo = res.data.data.shopInfo
                    setUpService.setSetUpInfo(res.data.data.setUpInfo);
                    this.$router.push('/summary');
                } else {
                    this.message = res.data.message;
                }
            });
        }
    }
};

const summaryView = {
    name: 'Summary',
    template: '#app-summary-view',
    mounted() {
        console.log('summary mounted');
        console.log(setUpService.setUpInfo);
        console.log(setUpService.ownerInfo);
        console.log(setUpService.shopInfo);
    },
    data() {
        return {
            setUpInfo: setUpService.setUpInfo,
            owner: setUpService.ownerInfo,
            shop: setUpService.shopInfo,
            atc: false,
            message: null
        }
    },
    methods: {
        proceed() {
            this.message = null;
            setUpService.acceptTermsAndConditions(this.atc).then(res => {
                console.log(res);
                if(res.data.type === 0) {
                    setUpService.setSetUpInfo(res.data.data.setUpInfo);
                    this.$router.push('/home');
                } else {
                    this.message = res.data.message;
                }
            });
        }
    }
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
        })
    }
});

app.use(router);

app.mount('#app');