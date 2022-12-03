function hideError(fieldInvalid, field) {
    fieldInvalid.innerHTML = ''
    fieldInvalid.classList.add('hidden')
    field.classList.remove('is-invalid')
}

function showError(fieldInvalid, field, text) {
    fieldInvalid.innerHTML = text
    fieldInvalid.classList.remove('hidden')
    field.classList.add('is-invalid')
}