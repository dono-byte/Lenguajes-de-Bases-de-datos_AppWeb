{
  "info": {
    "name": "TiendaApp - Resumen Clases 1 a 4",
    "_postman_id": "tiendaapp-collection",
    "description": "Coleccion de peticiones para probar TiendaApp.\n\nImportar: File -> Import -> seleccionar este .json",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    {
      "key": "base_url",
      "value": "http://localhost:8080",
      "type": "string"
    }
  ],
  "item": [
    {
      "name": "Home",
      "item": [
        {
          "name": "GET / (default)",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{base_url}}/",
              "host": ["{{base_url}}"],
              "path": [""]
            }
          }
        },
        {
          "name": "GET / con ?nombre",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{base_url}}/?nombre=Esteban",
              "host": ["{{base_url}}"],
              "path": [""],
              "query": [{"key": "nombre", "value": "Esteban"}]
            }
          }
        }
      ]
    },
    {
      "name": "Productos",
      "item": [
        {
          "name": "GET /productos (listar todos)",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{base_url}}/productos",
              "host": ["{{base_url}}"],
              "path": ["productos"]
            }
          }
        },
        {
          "name": "GET /productos/1 (detalle)",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{base_url}}/productos/1",
              "host": ["{{base_url}}"],
              "path": ["productos", "1"]
            }
          }
        },
        {
          "name": "GET /productos?buscar=laptop",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{base_url}}/productos?buscar=laptop",
              "host": ["{{base_url}}"],
              "path": ["productos"],
              "query": [{"key": "buscar", "value": "laptop"}]
            }
          }
        },
        {
          "name": "GET /productos?categoria=Electronica",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{base_url}}/productos?categoria=Electronica",
              "host": ["{{base_url}}"],
              "path": ["productos"],
              "query": [{"key": "categoria", "value": "Electronica"}]
            }
          }
        },
        {
          "name": "GET /productos/bajo-stock",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{base_url}}/productos/bajo-stock",
              "host": ["{{base_url}}"],
              "path": ["productos", "bajo-stock"]
            }
          }
        },
        {
          "name": "GET /productos/999 (no encontrado)",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{base_url}}/productos/999",
              "host": ["{{base_url}}"],
              "path": ["productos", "999"]
            }
          }
        }
      ]
    }
  ]
}
