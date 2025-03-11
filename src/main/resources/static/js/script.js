document.addEventListener("DOMContentLoaded", function () {
    setTimeout(() => {
        obtenerUsuarios();
    }, 500); // Retraso breve para asegurar que la página cargue completamente
});

function obtenerUsuarios() {
    fetch("http://localhost:8080/admin/all")
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al obtener los usuarios");
            }
            return response.json();
        })
        .then(data => {
            let tbody = document.getElementById("adminTableBody");
            if (!tbody) {
                console.error("No se encontró el elemento adminTableBody.");
                return;
            }

            tbody.innerHTML = ""; // Limpiar la tabla antes de agregar datos

            data.forEach(admin => {
                let fila = document.createElement("tr");
                fila.innerHTML = `
                    <td>${admin.id}</td>
                    <td>${admin.user ? admin.user.nombre || "Sin nombre" : "Sin usuario"}</td>
                    <td>${admin.user ? admin.user.apellido || "Sin apellido" : "Sin usuario"}</td>
                    <td>${admin.user ? admin.user.documento || "Sin documento" : "Sin usuario"}</td>
                    <td>${admin.user ? admin.user.correo || "Sin correo" : "Sin usuario"}</td>
                    <td>${admin.phone_number || "Sin teléfono"}</td>
                    <td>${admin.user && admin.user.roles ? admin.user.roles.join(", ") : "Sin rol"}</td>
                    <td>
                        <div class="button-container">
                            <button class="edit-btn" onclick="editarUsuario(${admin.id})">
                                <i class="fa fa-pencil"></i>
                            </button>
                            <button class="delete-btn" onclick="eliminarUsuario(${admin.id})">
                                <i class="fa fa-trash-o"></i>
                            </button>
                        </div>
                    </td>
                `;
                tbody.appendChild(fila);
            });
        })
        .catch(error => console.error("Error al obtener usuarios:", error));
}

// Función para redirigir a la edición de usuario
function editarUsuario(id) {
    window.location.href = `/usuarios/editar/${id}`;
}

// Función para eliminar usuario con confirmación
function eliminarUsuario(id) {
    Swal.fire({
        title: "¿Estás seguro?",
        text: "Esta acción no se puede deshacer",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`http://localhost:8080/admin/delete/${id}`, { method: "DELETE" })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Error al eliminar usuario");
                    }
                    return response.text();
                })
                .then(() => {
                    Swal.fire("Eliminado", "El usuario ha sido eliminado", "success");
                    obtenerUsuarios(); // Recargar la tabla
                })
                .catch(error => console.error("Error al eliminar:", error));
        }
    });
}
