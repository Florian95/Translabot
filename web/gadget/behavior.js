function init() {
  if (wave && wave.isInWaveContainer()) {
      document.getElementById('buffer').value = wave.getState().get('id-defined');
  } else {
      document.getElementById('buffer').value = "NOTHING";
  }
}
gadgets.util.registerOnLoadHandler(init);

document.getElementById('buffer').value = 'SOMETHING';
