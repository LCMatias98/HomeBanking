const { createApp } = Vue;

createApp({
  data() {
    return {
      clients: [],
      dolarOficial: [],
      loans:[],
      accounts:[]
    }
  },
  mounted() {
    const cantidadPesos = document.getElementById("cantidad-pesos");
    const tipoCambio = document.getElementById("tipo-cambio");
    const cantidadDolares = document.getElementById("cantidad-dolares");
    const formulario = document.getElementById("formulario");

    formulario.addEventListener("submit", (e) => {
      e.preventDefault();
      const cantidad = parseFloat(cantidadPesos.value);
      const cambio = parseFloat(tipoCambio.value);
      const resultado = cantidad * cambio;
      cantidadDolares.value = resultado.toFixed(2);
    });

    axios.get('/api/clients/current')
      .then(res => {
        console.log(res);
        this.clients = res.data;
        this.loans = this.clients.loans;
        this.accounts = this.clients.accounts;
        console.log(this.accounts)
      })
      .catch(error => {
        console.error(error);
      });

    axios.get("https://www.dolarsi.com/api/api.php?type=valoresprincipales")
      .then(res => {
        this.dolarOficial = res.data[0].casa.compra;
        console.log(this.dolarOficial);
      })
      .catch(error => {
        console.error(error);
      });
  },
/*   /clients/current/accounts */
  methods:{

    logOut() {
      axios.post('/api/logout')
        .then(response => {
          window.location.href = './index.html';
        })
        .catch(error => {
          console.error(error);
        });
    },

    crearAccount() {
      $('#confirmationModal').modal('show');
    
      $('#confirmButton').on('click', () => {
        axios.post('/api/clients/current/accounts')
          .then(response => {
            $('#confirmationModal').modal('hide'); // Cerrar el modal después de la confirmación
            this.showNotification('Account Created', 'success');
            setTimeout(() => {
              window.location.href = './accounts.html'; // Redireccionar después de un retraso
            }, 700);
            console.log(response.status);
          })
          .catch(error => {
            // Manejo de errores
            console.error(error);
          });

         
      });
      $('#cancelButton').on('click', () => {
        this.cancelTransfer(); // Llamar al método cancelTransfer cuando se haga clic en el botón de cancelación
      });
    
    },
    
    cancelTransfer() {
      $('#confirmationModal').modal('hide'); // Ocultar el modal cuando se cancele
    },
    
    showNotification(message, type) {
      const toast = document.createElement('div');
      toast.classList.add('toastify', type); // Agregar la clase "type"
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
    
    
  }
}).mount('#app');
