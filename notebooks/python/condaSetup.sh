#!/bin/bash

echo "installing conda .."

conda create -n pyclj python=3.7
# conda install -n pyclj numpy mxnet \ && 
# echo "source activate pyclj" > /home/$USERNAME/.bashrc 
## To install pip packages into the pyclj environment do
conda run -n pyclj python3 -mpip install numpy
# ~/.conda/envs/pyclj

conda run -n pyclj python3 -mpip install \
  seaborn matplotlib sklearn numpy pandas \
  umap-learn trimap 
  
# seems to be included above somewhere
# mxnet
  
# this does not work:  
#   linspace