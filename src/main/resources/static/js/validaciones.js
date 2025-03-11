// Selecciona el formulario
const form = document.querySelector('form');

// Agrega un evento de envío al formulario
form.addEventListener('submit', function(event) {
  // Previene el envío por defecto del formulario
  event.preventDefault();

  // Obtiene los valores de los campos
  const nombre = document.getElementById('nombre').value;
  const contraseña = document.getElementById('contraseña').value;

  // Valida que los campos no estén vacíos
  if (nombre === '' || contraseña === '') {
    alert('Los campos no pueden estar vacíos');
    return;
  }

  // Valida que el nombre no contenga números
  if (/\d/.test(nombre)) {
    alert('El nombre no puede contener números');
    return;
  }

  // Valida que la contraseña tenga al menos 8 caracteres
  if (contraseña.length < 8) {
    alert('La contraseña debe tener al menos 8 caracteres');
    return;
  }

  

  // Redirecciona a la página de inicio
  window.location.href = 'inicio';
});