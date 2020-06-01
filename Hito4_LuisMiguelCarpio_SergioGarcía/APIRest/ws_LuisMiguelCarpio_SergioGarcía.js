const express = require('express');
const app = express();	//Uso de express como servicio
const logger = require('morgan');	//Acceso a un log
const http = require('http');	//Funcionamiento con http
const path = require('path');	//Necesario para indicar rutas
const PORT = process.env.PORT || 8080;	//Ruta especifica o por defecto
const bodyParser = require('body-parser');	//Conversion de formatos
const baseAPI = '/api/v1';	//Posible manejo de versiones
const audienceService = require('./routes/audience-service');	//BD Audience
const audience = require('./routes/audience');	//Funciones de Audience
const cors = require('cors');	//Gestor de permisos

app.use(cors());
app.use(bodyParser.json());
app.use(logger('dev'));
app.use(bodyParser.urlencoded({
	extended: true
}));
app.use('/audience', audience);

app.get('/', function (req, res) {	//GET generico del servidor
	res.send('¡API en funcionamiento!');
});

const server = http.createServer(app);

audienceService.connectDb(function (err) {	//Conectado a la BD Audience
	if(err){
		console.log('Could not connect with MongoDB - audienceService');
		process.exit(1);
	}
	
	server.listen(PORT, function() {	//Escuchando en el puerto elegido
		console.log('Server up and running on localhost:' + PORT);
	});
});