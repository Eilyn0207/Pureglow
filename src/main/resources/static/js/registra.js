// Selecciona el formulario
const form = document.querySelector('form');

// Agrega un evento de envío al formulario
form.addEventListener('submit', function(event) {
  // Previene el envío por defecto del formulario
  event.preventDefault();

  // Obtiene los valores de los campos
  const nombre = document.getElementById('nombre').value;
  const apellido = document.getElementById('apellido').value;
  const email = document.getElementById('email').value;
  const contraseña = document.getElementById('contraseña').value;
  const confirmarContraseña = document.getElementById('confirmar-contraseña').value;
  const telefono = document.getElementById('telefono').value;
  const documento = document.getElementById('documento').value;

  // Valida que los campos no estén vacíos
  if (nombre === '' || apellido === '' || email === '' || contraseña === '' || confirmarContraseña === '' || telefono === '' || documento === '') {
    alert('Los campos no pueden estar vacíos');
    return;
  }

  // Valida que el nombre y apellido no contengan números
  if (/\d/.test(nombre) || /\d/.test(apellido)) {
    alert('El nombre y apellido no pueden contener números');
    return;
  }

  // Valida que el email sea válido
  if (!/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email)) {
    alert('El email no es válido');
    return;
  }

  // Valida que la contraseña y la confirmación de contraseña coincidan
  if (contraseña !== confirmarContraseña) {
    alert('La contraseña y la confirmación de contraseña no coinciden');
    return;
  }

  // Valida que el teléfono y el documento solo contengan números y no estén vacíos
  if (!/^[0-9]+$/.test(telefono) || !/^[0-9]+$/.test(documento) || telefono === '' || documento === '') {
    alert('El teléfono y el documento solo pueden contener números y no deben estar vacíos');
    return;
  }

  // Redirecciona a la página de iniciodesesion
  window.location.href = 'sesion';
});