document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.alert').forEach(el => {
        setTimeout(() => {
            el.classList.add('fade');
            el.addEventListener('transitionend', () => el.remove());
        }, 4000);
    });
});
