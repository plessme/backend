#!/bin/bash
set -e

docker run \
    -v ~/Work/plessme/backend:/workspace \
    postman/newman:5.0.0-ubuntu /workspace/src/test/api/users-api-test-collection.json \
    --environment="/workspace/src/test/api/api-test-build-env.json" \
    --reporters junit \
    --reporter-junit-export="workspace/build/test-results/test/newman-report.xml" 