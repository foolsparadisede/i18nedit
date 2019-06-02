#!/bin/bash

cd ../frontend/

if [$(which yarn)]; then
    yarn install
else
    npm install
fi

ng build --prod