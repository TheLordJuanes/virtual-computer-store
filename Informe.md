# Informe
**Integrantes**
- Jose Alejandro García Marcos — A00365583.
- Juan Esteban Caicedo Alzate — A00365977.
- Juan Pablo Ramos Torres— A00368429.
## Cómo hicimos el programa
Tomamos un proyecto que creaba una plataforma de venta y compra de computadores y lo adaptamos para que cumpliera las necesidades que se nos pedía en el enunciado. Así, añadimos los siguientes cambios:
- El administrador ya no puede cambiar el rol de un usuario básico a administrador.
- El administrador puede eliminar usuarios básicos.
- El administrador puede resetear (cambiar) la contraseña de un usuario a una temporal.
- Los usuarios básicos pueden ver su última fecha y hora de inicio de sesión.
- Los usuarios básicos pueden cambiar su contraseña.
- Las contraseñas se almacenan con salt, a través del algoritmo PBKDF2 para el hashing.
## Dificultades
Nos hubiera gustado usar una librería o API para realizar el cambio de contraseña de modo que se enviara un correo o algo parecido al usuario para que siguiera un enlace y estableciera su contraseña pero, por cuestiones de tiempo, adoptamos un mecanismo más manual y humano en el que el administrador provee una contraseña provisional y por canales externos ajenos al sistema, le informa al usuario de la situación, le da su contraseña y le pide que la actualice lo antes posible.
También quisimos implementar Spring Security pero nos fue imposible adaptar el proyecto.
## Conclusiones
En definitiva, luego de realizar este proyecto final con la funcionalidad de login para esta plataforma de tienda virtual de computadores y considerando los requisitos establecidos, se pueden extraer las siguientes conclusiones:

1. Relevancia del uso de técnicas de hashing y salting: El almacenamiento seguro de las contraseñas fue crucial para proteger las cuentas de los usuarios. En efecto, la utilización del algoritmo PBKDF2 para el hashing de las contraseñas, combinado con el uso de un salt único para cada usuario, mejoró la seguridad de las contraseñas almacenadas, pues antes se guardaban en texto plano en la base de datos PostgreSQL; además dificulta su recuperación en caso de un ataque de fuerza bruta o diccionario.
2. Importancia de la gestión de usuarios y privilegios: La distinción entre el administrador y los usuarios comunes nos permitió un control adecuado de los permisos de cada rol en la plataforma.
3. Nos alegra ver que algoritmos tan importantes y útiles como el de PBKDF2 están disponibles abiertamente (de forma fácil de adoptar) y aún así sean seguros.
