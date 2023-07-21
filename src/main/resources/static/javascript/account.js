const { createApp } = Vue;

createApp({
  data() {
    return {
      data: [],
      transaction:[],
      dateWhitAccount: {
        dateStart: '',
        dateEnd:'',
      },
      fechaInicio:'',
      fechaFin:'',
      params: "",
      err: '',
      showConfirmation: false
    }
  },

  created(){
        this.params = new URLSearchParams(location.search).get("id");
        console.log(this.params);
        this.cuentaCliente();

  },

  methods: {
    cuentaCliente(){
      axios.get(`/api/accounts/`+this.params)
        .then(res => {
          this.data = res.data;
          this.transaction = this.data.transaction.sort((a , b) => b.id - a.id );
          console.log(this.data)
        })
        .catch(error => {
          console.error(error);
        });
      },

    print() {
      this.showConfirmation = true;
    },

    confirmPrint() {
      this.showConfirmation = false;
    
      const transferData = {
        id: this.params,
        localDateTimeStart: this.dateWhitAccount.dateStart,
        localDateTimeEnd: this.dateWhitAccount.dateEnd
      };
    
      axios.post('/api/transactions/PDF', transferData, { responseType: 'arraybuffer' }) // Set the responseType to 'arraybuffer'
        .then(res => {
          console.log(res);
          this.status = res.status;
          if (this.status === 200) { 
            // Crear un blob a partir de los datos de respuesta para crear un archivo descargable
            const blob = new Blob([res.data], { type: 'application/pdf' });
    
           // Crea una URL temporal para el objeto Blob
            const url = URL.createObjectURL(blob);
    
            // Crear un elemento de enlace para activar la descarga
            const link = document.createElement('a');
            link.href = url;
            link.download = 'Transactions.pdf';
            link.click();
    
           // Limpiar la URL temporal
            URL.revokeObjectURL(url);
    
            this.showNotification('Transaction success', 'success');

          }
        })
        .catch(error => {
          console.error(error);
          this.err = error.response.data;
          console.log(this.err);
          this.showNotification(this.err, 'error');
        });
    },
   
    cancelPrint() {
      this.showConfirmation = false;
    },  

    logOut(){
      axios.post('/api/logout')
      .then(res => {
        window.location.href = './index.html';
      })
      .catch(error => {
        console.error(error);
      });
    },

    showNotification(message, type) {
      const toast = document.createElement('div');
      toast.classList.add('toastify', type);
      toast.textContent = message;
      document.body.appendChild(toast);

      setTimeout(() => {
        toast.classList.add('show');
        setTimeout(() => {
          toast.classList.remove('show');
          setTimeout(() => {
            document.body.removeChild(toast);
          }, 300);
        }, 2000);
      }, 100);
    }

  },
}).mount('#app');
