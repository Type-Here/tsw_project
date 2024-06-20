/**
 * OnLoad Listener in Login Form Set Redirect if present
 */
document.getElementById('login').addEventListener('submit', function (e){
    e.preventDefault();
    const params = new URLSearchParams(window.location.search);
    console.log(params);
    if(params.get("redirect")){
       this.action += '?redirect=' + params.get("redirect");
       console.log(this.action);
    }
    this.submit();
});