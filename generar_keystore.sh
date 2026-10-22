#!/bin/bash
# ==============================================================
# Script para generar el keystore de firma del APK
# Belmonte SpA - Transporte Corporativo
# Responsabilidad Transversal: APK Firmado
#
# NOTA: Este keystore es FICTICIO para el MVP educativo.
# En producción se usaría un keystore real con credenciales seguras.
#
# Curso: DSY1105 - Duoc UC
# ==============================================================

echo "Generando keystore ficticio para Belmonte Traslados..."

keytool -genkeypair \
  -v \
  -keystore app/belmonte-release-key.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias belmonte-key \
  -storepass belmonte2026 \
  -keypass belmonte2026 \
  -dname "CN=Belmonte SpA, OU=Desarrollo, O=Belmonte SpA, L=Santiago, ST=RM, C=CL"

echo ""
echo "Keystore generado exitosamente en: app/belmonte-release-key.jks"
echo "Alias: belmonte-key"
echo "Contraseña: belmonte2026"
echo ""
echo "Para generar el APK firmado en Android Studio:"
echo "  Build > Generate Signed Bundle / APK > APK"
echo "  Seleccionar el keystore generado."
