const { createApp } = Vue
 
createApp({
    data() {
      return {

        }
      },
    created(){
    },

    methods:{
        logOut(){
          axios.post('/api/logout')
          .then(res => {
            window.location.href = './index.html';
          })
          .catch(error => {
            console.error(error);
          });
        }
    },
})
.mount('#app');