document.addEventListener('DOMContentLoaded',async (e)=>{
    try {
        const resp = await axios.post("/track-visitor",{
            headers : {
                "Content-Type" : "application/json"
            }
        })
    } catch (e) {

    }
})