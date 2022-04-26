const registerOwnerView = {
    name: 'Register Owner',
    template: '#app-register-owner-view',
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
            if(setUpService.setUpInfo.steps['register-owner']) {
                this.$router.push('/create-shop');
                return;
            }

            this.message = null;
            this.item.token = this.item.password;
            // console.log(this.item);
            setUpService.registerOwner(this.item).then(res => {
                console.log(res);
                if(res.data.type === 0) {
                    setUpService.setSetUpInfo(res.data.data);
                    this.$router.push('/create-shop');
                } else {
                    this.message = res.data.message;
                }
            });
        }
    }
};
