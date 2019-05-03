/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var socket;
var localStream;
var started = false;
var pc;
const configuration = {iceServers: [{urls: 'stuns:stun.l.googke.com:19302'}]};
const constraints = window.constraints = {
  audio: false,
  video: true
};


function start(){
    const video = document.getElementById('video');
    // Get access to the camera!
    if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
        // Not adding `{ audio: true }` since we only want video now
        navigator.mediaDevices.getUserMedia(constraints)
        .then(function(stream) {
            //video.src = window.URL.createObjectURL(stream);
            video.srcObject = stream;
            localStream = stream;
            video.play();
            started = true;
        })
        .catch(function(error) {
            /* handle the error */
            if (error.name === 'ConstraintNotSatisfiedError') {
                let v = constraints.video;
                errorMsg(`The resolution ${v.width.exact}x${v.height.exact} px is not supported by your device.`);
            } else if (error.name === 'PermissionDeniedError') {
                errorMsg('Permissions have not been granted to use your camera and ' +
                'microphone, you need to allow the page access to your devices in ' +
                'order for the demo to work.');
            }
            errorMsg(`getUserMedia error: ${error.name}`, error);
        });
    }
    if (!started && localStream) {
        console.log("Creating PeerConnection.");
        pc = new RTCPeerConnection(configuration);
        console.log("Adding local stream.");
        pc.addStream(localStream);
        started = true;
        // Caller initiates offer to peer.
        console.log("Sending offer to peer.");
        var offer = pc.createOffer(constraints);
        pc.setLocalDescription(pc.SDP_OFFER, offer);
        sendMessage({
            user : 'start',
            type : 'offer',
            sdp : offer.toSdp()
        });
        pc.startIce();
    }
}

function setLocalAndSendMessage(sessionDescription) {
    pc.setLocalDescription(sessionDescription);
    sendMessage(sessionDescription);
}

function errorMsg(msg, error) {
    const errorElement = document.querySelector('#errorMsg');
    errorElement.innerHTML = `<p>${msg}</p>`;
    if (typeof error !== 'undefined') {
      console.error(error);
    }
}

function stream(){
    socket = new WebSocket("ws://127.0.0.1:8084/video/10/start");
    socket.onopen = function (event) {
        console.log("/!\\ Connexion serveur");
    };
    socket.onerror = function (event) {
        console.log(event);
    };
    socket.onmessage = function (event) {
        if (event.data instanceof ArrayBuffer) {
        } else {
           $('#affiche').html(event.data);
        }
    };
    socket.onclose = function (event) {
        sendMessage({
            user : 'start',
            type : 'bye'
        });
        console.log("/!\\ Déconnexion serveur");
    };
}

function sendMessage(message){
    var msgString = JSON.stringify(message);
    console.log('message sent : ' + msgString);
    socket.send(msgString);
}
