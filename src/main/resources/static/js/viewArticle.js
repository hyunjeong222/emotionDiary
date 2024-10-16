document.addEventListener('DOMContentLoaded', () => {
    const backButton = document.getElementById('button-back');

    if(backButton){
        backButton.addEventListener('click', e => {
            window.history.back();
        });
    }
});