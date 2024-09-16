Feature: Operaciones CRUD de Usuarios
  Como usuario de la API
  Quiero gestionar usuarios (crear, leer, actualizar, eliminar) AND realizar autenticación
  Para poder mantener los datos de los usuarios AND asegurar el acceso

  Background:
    GIVEN que el servidor está en ejecución

  Scenario: Actualización exitosa de usuario
    GIVEN Un usuario autenticado
    AND intenta actualizarse a si mismo
    WHEN se envía una solicitud de actualizacion con los datos
    THEN el estado de la respuesta debe ser 200
    AND el mensaje de respuesta debe ser "Usuario actualizado"

  Scenario: Fallo al actualizar un usuario
    GIVEN Un usuario autenticado
    AND intenta actualizar otro usuario
    WHEN se envía una solicitud de actualizacion con los datos
    THEN el estado de la respuesta debe ser 401
    AND el mensaje de respuesta debe ser "Error: Usuario no autorizado"

  Scenario: Fallo al actualizar un usuario existente
    GIVEN Un usuario autenticado
    AND intenta actualizar un usuario
    WHEN se envía una solicitud de actualizacion sin los datos
    THEN el estado de la respuesta debe ser 400
    AND el mensaje de respuesta debe ser "Ambos datos a actualizar son nulos"

  Scenario: Fallo al actualizar un usuario existente por falta de permisos
    GIVEN Un usuario no autenticado
    AND intenta actualizar un usuario
    WHEN se envía una solicitud de actualizacion con los datos
    THEN el estado de la respuesta debe ser 401
    AND el mensaje de respuesta debe ser "Error: Usuario no autorizado"