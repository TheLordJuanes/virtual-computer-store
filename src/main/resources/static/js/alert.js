function customAlert(icon, title, text, url) {
    Swal.fire({icon, title, text}).then((result) => {
        if (result.isConfirmed) {
            window.location = url
        }
    })
}