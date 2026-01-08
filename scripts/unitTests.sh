set -e

bash build.sh

cd "$(dirname "$0")/.." || exit

mvn test