const homeView = {
    name: 'Home',
    template: '#app-home-view',
    data() {
        var o = {
            userId: setUpService.sadminInfo.userId,
            password: setUpService.sadminInfo.password
        };
        o.password = 'salestap4321'
        return {
            setUpInfo: setUpService.setUpInfo,
            item: o,
            message: null
        }
    },
    mounted() {
        var self = this;
        setTimeout(function() {
            self.$forceUpdate();
        }, 2000);
    },
    methods: {
        auth() {
            this.message = null;
            // console.log(this.item);
            setUpService.auth(this.item).then(res => {
                console.log(res);
                if(res.data.type === 0) {
                    setUpService.setSetUpInfo(res.data.data);
                    this.setUpInfo = setUpService.setUpInfo;
                    if(this.setUpInfo.state === 'Completed') {
                        this.$router.push('/summary');
                    } else {
                        if(this.setUpInfo.steps['create-shop'])
                            this.$router.push('/summary');
                        else if(this.setUpInfo.steps['register-owner'])
                            this.$router.push('/create-shop');
                        else
                            this.$router.push('/register-owner');
                    }
                }
                else
                    this.message = res.data.message;
            });
        }
    }
};
