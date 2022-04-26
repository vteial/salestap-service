const summaryView = {
    name: 'Summary',
    template: '#app-summary-view',
    data() {
        return {
            setUpInfo: setUpService.setUpInfo,
            owner: setUpService.setUpInfo.owner,
            shop: setUpService.setUpInfo.shop,
            atc: setUpService.setUpInfo.termsAndConditions,
            message: null
        }
    },
    methods: {
        proceed() {
            if(setUpService.setUpInfo.steps['summary']) {
                this.$router.push('/home');
                return;
            }
            this.message = null;
            setUpService.acceptTermsAndConditions(this.atc).then(res => {
                console.log(res);
                if(res.data.type === 0) {
                    setUpService.setSetUpInfo(res.data.data);
                    this.$router.push('/home');
                } else {
                    this.message = res.data.message;
                }
            });
        }
    }
};
