Feature: Operaciones CRUD de Usuarios
  Como usuario de la API
  Quiero gestionar usuarios (crear, leer, actualizar, eliminar) AND realizar autenticación
  Para poder mantener los datos de los usuarios AND asegurar el acceso

  Background:
    GIVEN que el servidor está en ejecución  
  
  Scenario: Eliminación exitosa de usuario
    GIVEN Un usuario autenticado
    AND intenta eliminarse a si mismo
    WHEN se envía una solicitud de eliminacion con el usuario
    THEN el estado de la respuesta debe ser 200
    AND el mensaje de respuesta debe ser "Usuario eliminado"

  Scenario: Fallo al eliminar un usuario
    GIVEN Un usuario autenticado
    AND intenta eliminar otro usuario
    WHEN se envía una solicitud de eliminacion con el usuario
    THEN el estado de la respuesta debe ser 401
    AND el mensaje de respuesta debe ser "Error: Usuario no autorizado"

  Scenario: Fallo al eliminar un usuario no existente
    GIVEN Un usuario autenticado
    AND intenta eliminar un usuario no existente
    WHEN se envía una solicitud de eliminacion con el usuario
    THEN el estado de la respuesta debe ser 404
    AND el mensaje de respuesta debe ser "El usuario a eliminar no fue encontrado"

  Scenario: Fallo al eliminar un usuario existente
    GIVEN Un usuario no autenticado
    AND intenta eliminar un usuario
    WHEN se envía una solicitud de eliminacion con el usuario
    THEN el estado de la respuesta debe ser 401
    AND el mensaje de respuesta debe ser "Error: Usuario no autorizado"