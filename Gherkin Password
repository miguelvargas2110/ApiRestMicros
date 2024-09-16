Feature: Operaciones CRUD de Usuarios
  Como usuario de la API
  Quiero gestionar usuarios (crear, leer, actualizar, eliminar) AND realizar autenticación
  Para poder mantener los datos de los usuarios AND asegurar el acceso

  Background:
    GIVEN que el servidor está en ejecución

  Scenario: Cambio de contraseña exitoso
    GIVEN Un usuario valido para hacer el cambio de contraseña
    WHEN se envía una solicitud de cambio de contraseña con una nueva contraseña
    THEN el estado de la respuesta debe ser 200
    AND el mensaje de respuesta debe ser "Contraseña actualizada"

  Scenario: Fallo al cambiar la contraseña
    GIVEN Un usuario invalido para hacer el cambio de contraseña
    WHEN se envía una solicitud de cambio de contraseña con una nueva contraseña
    THEN el estado de la respuesta debe ser 401
    AND el mensaje de respuesta debe ser "Error: Usuario no autorizado"

  Scenario: Generación de token para cambio de contraseña
    GIVEN Un usuario existente
    WHEN se envía una solicitud al servicio de generacion de token para cambio de contraseña con el usuario
    THEN el estado de la respuesta debe ser 200
    AND se debe devolver un token

  Scenario: Fallo al general el token para cambio de contraseña
    GIVEN Un usuario no existente
    WHEN se envía una solicitud al servicio de generacion de token para cambio de contraseña con el usuario
    THEN el estado de la respuesta debe ser 404
    AND el mensaje de respuesta debe ser "Usuario no encontrado"