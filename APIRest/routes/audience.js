'use strict';

const express = require('express');
const router = express.Router(); //Permite la ramificacion de los servicios
const audienceService = require('./audience-service');

//Devolucion de getAll considerando posibles errores y coleccion vacia
router.get('/', function (req, res) {
	audienceService.getAll((err, audience) => {
		if(err){
			res.status(500).send({msg: err});
		} else if (audience === null){
			res.status(500).send({msg: "No hay asistentes"});
		} else {
			res.status(200).send(audience);
		}
	});
});

//Devolucion de get considerando posibles errores y elemento inexistente
router.get('/:dni', function (req, res) {
	let dni = req.params.dni;
	audienceService.get(dni, (err, person) => {
		if(err){
			res.status(500).send({msg: err});
		} else if(person === null){
			res.status(500).send({msg: 'No existe dicho asistente'});
		} else {
			res.status(200).send(person);
		}
	});
});

//Devolucion de add considerando posibles errores y elemento no vacio
router.post('/', function (req, res) {
	let person = req.body;
	audienceService.add(person, (err, person) => {
		if(err){
			res.status(500).send({msg: err});
		} else if(person !== null){
			res.send({msg: 'Asistente añadido'});
		}
	});
});

//Devolucion de update considerando posibles errores y no actualizacion
router.put('/:dni', function (req, res) {
	const dni = req.params.dni;
	const updatedPerson = req.body;
	audienceService.update(dni, updatedPerson, (err, numUpdates) => {
		if(err || numUpdates === 0) {
			res.status(500).send({msg: err});
		} else {
			res.status(200).send({msg: 'Asistente modificado'});
		}
	});
});

//Devolucion de removeAll considerando posibles errores
router.delete('/', function (req, res) {
	audienceService.removeAll((err) => {
		if(err){
			res.status(500).send({msg: err});
		} else {
			res.status(200).send({msg: 'Se han eliminado todos los asistentes'});
		}
	});
});

//Devolucion de remove considerando posibles errores
router.delete('/:dni', function (req, res) {
	let dni = req.params.dni;
	audienceService.remove(dni, (err) => {
		if(err){
			res.status(404).send({msg: err});
		} else {
			res.status(200).send({msg: 'Asistente eliminado'});
		}
	});
});

module.exports = router;