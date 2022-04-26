const createShopView = {
    name: 'Create Shop',
    template: '#app-create-shop-view',
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
