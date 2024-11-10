import React, { useState, useEffect } from 'react';
import axios from 'axios';
import SelectCenter from './SelectCenter';

function Location1() {
    const url = 'http://localhost:5000/address';

    const [states, setStates] = useState({});

    useEffect(() => {
        const fetchData = async () => {
            const data = await fetchAddresses();
            setStates(data);
        };
        fetchData();
    }, []);


    const fetchAddresses = async () => {
        try {
            const response = await axios.get(url);
            const addresses = response.data;


            return addresses;
        } catch (error) {
            console.error('Error fetching data from db.json:', error);
            return [];
        }
    };

    return (
        <div>

            {states.length >= 1 && <SelectCenter states={states}></SelectCenter>}


        </div>


    );
}

export default Location1;
