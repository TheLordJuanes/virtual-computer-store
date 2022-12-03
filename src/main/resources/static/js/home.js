const size = document.querySelector('.size')

const expressions = {
    quantity: /^[0-9]+$/
}

for (let i = 1; i <= size.value; i++) {
    const home = document.querySelector('.home-' + i)
    const quantity = document.querySelector('.quantity-' + i)
    const invalidQuantity = document.querySelector('.invalidQuantity-' + i)
    const add = document.querySelector('.add-' + i)

    home.addEventListener('change', () => {
        if(quantityIsValid()) {
            tooltipList[i-1].disable()
            add.disabled = false
        } else {
            tooltipList[i-1].enable()
            add.disabled = true
        }
    })

    function quantityIsValid() {
        return expressions.quantity.test(quantity.value) && quantity.value > 0
    }

    quantity.addEventListener('change', () => {
        quantityIsValid() ? hideError(invalidQuantity, quantity) : showError(invalidQuantity, quantity, 'Invalid quantity: cannot be empty and only positive numbers are allowed.')
    })
}