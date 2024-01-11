using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class movimiento : MonoBehaviour
{
	Rigidbody rb;
	Vector2 inputMov;
	Vector2 inputRot;
	public float velCamina = 50f;
	public float sensibilidad = 30f;
	public float fuerzaSalto=200f;
	Transform cam;
	float rotX;
    // Start is called before the first frame update
    void Start()
    {
        rb = GetComponent<Rigidbody>();
		cam = transform.GetChild(0);
		rotX= cam.eulerAngles.x;
		
		
		
    }

    // Update is called once per frame
    void Update()
    {
        inputMov.x= Input.GetAxis("Horizontal");
        inputMov.y= Input.GetAxis("Vertical");
		
		inputRot.x=Input.GetAxis("Mouse X") * sensibilidad;
		inputRot.y=Input.GetAxis("Mouse Y")* sensibilidad;
		
		if (Input.GetButtonDown("Jump")) rb.AddForce(0,fuerzaSalto,0);
    }
	private void FixedUpdate (){
		float vel= velCamina;
		rb.velocity = 
			transform.forward * vel * inputMov.y 
		    + transform.right * vel * inputMov.x
			+ new Vector3 (0,rb.velocity.y,0);
		
		transform.rotation *= Quaternion.Euler (0,inputRot.x,0);
		
		rotX-= inputRot.y;
		rotX= Mathf.Clamp(rotX,-50,50);
		cam.localRotation = Quaternion.Euler(rotX,0,0);
		
	}
}
