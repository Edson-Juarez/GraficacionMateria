using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class cambiocamaras : MonoBehaviour
{
	public GameObject[] listacamaras;
    // Start is called before the first frame update
    void Start()
    {
        listacamaras[0].gameObject.SetActive(true);
		listacamaras[1].gameObject.SetActive(false);
    }

    // Update is called once per frame
    void Update()
    {
        if(Input.GetKey(KeyCode.Alpha1)){
        listacamaras[0].gameObject.SetActive(true);
		listacamaras[1].gameObject.SetActive(false);
        }
        if(Input.GetKey(KeyCode.Alpha2)){
	        listacamaras[0].gameObject.SetActive(false);
			listacamaras[1].gameObject.SetActive(true);
        }
    }
}
