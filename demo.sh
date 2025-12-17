#!/bin/bash
# Script de Demo - CRUD Funcional
# Uso: bash demo.sh

echo "====================================="
echo "DEMO: CRUD Funcional - App Fútbol"
echo "====================================="
echo ""

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Base URLs
RIVALES_API="https://ms-rivales.onrender.com"
JUGADORES_API="https://ms-jugadores.onrender.com"
EQUIPOS_API="https://ms-equipos.onrender.com"
PARTIDOS_API="https://ms-partidos.onrender.com"

echo -e "${BLUE}1. DEMOSTRACIÓN - CREAR RIVAL${NC}"
echo "POST $RIVALES_API/rivales"
echo "Body: {\"nombre\":\"Real Madrid\"}"
echo ""
curl -X POST "$RIVALES_API/rivales" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Real Madrid"}' 2>/dev/null | jq '.'
echo ""
echo ""

echo -e "${BLUE}2. DEMOSTRACIÓN - LISTAR RIVALES${NC}"
echo "GET $RIVALES_API/rivales"
echo ""
curl -s "$RIVALES_API/rivales" | jq '.' | head -20
echo ""
echo ""

echo -e "${BLUE}3. DEMOSTRACIÓN - CREAR JUGADOR${NC}"
echo "POST $JUGADORES_API/jugadores"
echo "Body: {\"nombre\":\"Messi\",\"posicion\":\"Delantero\",\"dorsal\":10,\"edad\":37,\"equipoId\":1}"
echo ""
curl -X POST "$JUGADORES_API/jugadores" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Messi","posicion":"Delantero","dorsal":10,"edad":37,"equipoId":1}' 2>/dev/null | jq '.'
echo ""
echo ""

echo -e "${BLUE}4. DEMOSTRACIÓN - CREAR EQUIPO${NC}"
echo "POST $EQUIPOS_API/equipos"
echo "Body: {\"nombre\":\"Inter Miami\",\"entrenador\":\"Gerardo Martino\",\"escudoUrl\":\"...\"}"
echo ""
curl -X POST "$EQUIPOS_API/equipos" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Inter Miami","entrenador":"Gerardo Martino","escudoUrl":"url"}' 2>/dev/null | jq '.'
echo ""
echo ""

echo -e "${BLUE}5. DEMOSTRACIÓN - CREAR PARTIDO${NC}"
echo "POST $PARTIDOS_API/partidos"
echo "Body: {\"fecha\":\"2024-12-16\",\"rivalId\":1,\"resultado\":\"GANADO\",\"golesFavor\":3,\"golesContra\":1}"
echo ""
curl -X POST "$PARTIDOS_API/partidos" \
  -H "Content-Type: application/json" \
  -d '{"fecha":"2024-12-16","rivalId":1,"resultado":"GANADO","golesFavor":3,"golesContra":1}' 2>/dev/null | jq '.'
echo ""
echo ""

echo -e "${GREEN}✅ Demo completada${NC}"
echo ""
echo "Para más pruebas, ejecutar tests:"
echo "  ./gradlew test --tests RivalRepositoryTest"
echo "  ./gradlew test --tests PartidoRepositoryTest"

