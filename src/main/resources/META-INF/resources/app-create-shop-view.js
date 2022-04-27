const createShopView = {
    name: 'Create Shop',
    template: '#app-create-shop-view',
    data() {
        var o = {
            code: null,
            name: null,
            aliasName: null,
        };
         if(setUpService.appInfo.mode != 'normal') {
            o = {
                code: 'shop-ho',
                name: 'National Super Market',
                aliasName: 'Main Branch',
            }
        } else {
            o = setUpService.setUpInfo.shop;
        }
        return {
            item: o,
            message: null
        }
    },
    methods: {
        proceed() {
            if(setUpService.setUpInfo.steps['create-shop']) {
                this.$router.push('/summary');
                return;
            }

            this.message = null;
            // console.log(this.item);
            setUpService.createShop(this.item).then(res => {
                console.log(res);
                if(res.data.type === 0) {
                    setUpService.setSetUpInfo(res.data.data);
                    this.$router.push('/summary');
                } else {
                    this.message = res.data.message;
                }
            });
        }
    }
};
