require("dotenv").config();

const app = require("./app");
const conectarBD = require("./config/db");

const PORT = process.env.PORT || 8080;

async function iniciarServidor() {
    await conectarBD();

    app.listen(PORT, () => {
        console.log(
            `API Festivos ejecutándose en http://localhost:${PORT}`
        );
    });
}

iniciarServidor();