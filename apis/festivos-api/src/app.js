const express = require("express");

const swaggerUi =
    require("swagger-ui-express");

const swaggerSpec =
    require("./config/swagger");

const festivoRoutes =
    require("./routes/festivo.routes");


const app = express();


// Permite recibir JSON
app.use(express.json());


// =========================
// SWAGGER
// =========================
app.use(
    "/api-docs",
    swaggerUi.serve,
    swaggerUi.setup(swaggerSpec)
);


// =========================
// RUTA PRINCIPAL
// =========================
app.get("/", (req, res) => {

    res.json({
        mensaje:
            "API Festivos funcionando"
    });
});


// =========================
// RUTAS DE FESTIVOS
// =========================
app.use(
    "/api/festivos",
    festivoRoutes
);


module.exports = app;