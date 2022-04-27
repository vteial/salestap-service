const setUpService = {
    appInfo: { name: 'SalesTap', mode: 'normal'},
    sadminInfo: { userId: 'sadmin', password: null },
    setUpInfo: {
        isAuthenticated: false,
        state: 'New',
        steps: { 'register-owner': false, 'create-shop': false, 'summary': false },
        termsAndConditions: false,
        owner: null,
        shop: null
   },
   getAndSetAppInfo: function() {
        http.get('/app-info').then(res => {
            this.appInfo.name = res.data.name;
            this.appInfo.mode = res.data.mode.toLowerCase();
            console.log(this.appInfo);
        });
   },
   getSetUpInfo: function() {
       return http.get('/set-up/current-state');
   },
   setSetUpInfo: function(item) {
       this.setUpInfo.isAuthenticated = item.authenticated;
       this.setUpInfo.state = item.state;
       this.setUpInfo.steps = item.steps;
       this.setUpInfo.owner = item.owner;
       this.setUpInfo.shop = item.shop;
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
       return http.get('/set-up/accept-terms-and-conditions?atc='+atc);
   }
}
