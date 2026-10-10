const swaggerJsdoc = require("swagger-jsdoc");
const path = require("path");


// Ruta donde Swagger buscara
// la documentación de los endpoints.
const rutasPath = path
    .resolve(
        __dirname,
        "../routes/*.js"
    )
    .replace(/\\/g, "/");


const opciones = {

    definition: {

        openapi: "3.0.0",

        info: {
            title: "API Festivos",
            version: "1.0.0",
            description:
                "API REST para gestionar y calcular fechas festivas a partir de la configuración almacenada en MongoDB."
        },

        servers: [
            {
                url: "http://localhost:8080",
                description:
                    "Servidor local"
            }
        ],

        components: {

            schemas: {

                Festivo: {
                    type: "object",

                    required: [
                        "dia",
                        "mes",
                        "nombre"
                    ],

                    properties: {

                        dia: {
                            type: "integer",
                            example: 6
                        },

                        mes: {
                            type: "integer",
                            example: 1
                        },

                        nombre: {
                            type: "string",
                            example:
                                "Santos Reyes"
                        },

                        diasPascua: {
                            type: "integer",
                            nullable: true,
                            example: 40
                        }
                    }
                },


                Tipo: {
                    type: "object",

                    properties: {

                        id: {
                            type: "integer",
                            example: 1
                        },

                        tipo: {
                            type: "string",
                            example: "Fijo"
                        },

                        modoCalculo: {
                            type: "string",
                            example:
                                "No se puede variar"
                        },

                        festivos: {
                            type: "array",

                            items: {
                                $ref:
                                    "#/components/schemas/Festivo"
                            }
                        }
                    }
                },


                FestivoCalculado: {
                    type: "object",

                    properties: {

                        nombre: {
                            type: "string",
                            example: "Navidad"
                        },

                        fecha: {
                            type: "string",
                            format: "date",
                            example: "2026-12-25"
                        }
                    }
                },


                VerificacionFecha: {
                    type: "object",

                    properties: {

                        fecha: {
                            type: "string",
                            format: "date",
                            example: "2026-12-25"
                        },

                        esFestivo: {
                            type: "boolean",
                            example: true
                        },

                        festivos: {
                            type: "array",

                            items: {
                                type: "string"
                            },

                            example: [
                                "Navidad"
                            ]
                        }
                    }
                },


                Error: {
                    type: "object",

                    properties: {

                        mensaje: {
                            type: "string",
                            example:
                                "La fecha no es válida"
                        }
                    }
                }
            }
        }
    },


    apis: [
        rutasPath
    ]
};


const swaggerSpec =
    swaggerJsdoc(opciones);


module.exports = swaggerSpec;