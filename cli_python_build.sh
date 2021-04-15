BUILD_NAME=cli-python-build
BUILD_NUMBER=4
MODULE_NAME=babel
DEPLOY_REPO=pypi

jfrog rt pipi -r requirements.txt --build-name=$BUILD_NAME --build-number=$BUILD_NUMBER --module=$MODULE_NAME
python setup.py sdist bdist_wheel

# just upload, then available in virtual repo's API for installation e.g. http://182.92.214.141:8081/artifactory/api/pypi/pypi/simple/babel/
jfrog rt u dist/ $DEPLOY_REPO --build-name=$BUILD_NAME --build-number=$BUILD_NUMBER
jfrog rt bp $BUILD_NAME $BUILD_NUMBER
