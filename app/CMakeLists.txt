
cmake_minimum_required(VERSION 3.4.1)



set(my_lib_path ${CMAKE_SOURCE_DIR}/src/main/jniLibs)

add_library( jpegbither
             SHARED
             IMPORTED )

set_target_properties( jpegbither
                       PROPERTIES IMPORTED_LOCATION
                       ${my_lib_path}/${ANDROID_ABI}/libjpegbither.so )

add_library(
             bitherlib
             SHARED
             src/main/cpp/bitherlibjni.cpp )

find_library(
              log-lib
              log )

target_link_libraries(
                       bitherlib
                       jnigraphics
                       jpegbither
                       ${log-lib} )