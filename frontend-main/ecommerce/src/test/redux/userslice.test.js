import { configureStore } from '@reduxjs/toolkit';
import loginReducer, { login, logout } from '../../redux/userSlice';


describe('loginSlice', () => {
    let store;

    beforeEach(() => {
        store = configureStore({
            reducer: {
                login: loginReducer,
            },
        });
    });

    test('should return the initial state', () => {
        const state = store.getState().login;
        expect(state).toEqual({
            login: {
                id: null,
                name: null,
                email: null,
                isLoggedIn: false,
                role: null,
            }
        });
    });
    

    test('should handle login', () => {
        store.dispatch(login({
            id: 1,
            name: 'John Doe',
            email: 'john.doe@example.com',
            role: 'admin',
            token: 'some-token'
        }));
    
        const state = store.getState().login;
        expect(state).toEqual({
            login: {
                id: 1,
                name: 'John Doe',
                email: 'john.doe@example.com',
                isLoggedIn: true,
                role: 'admin',
                token: 'some-token'
            }
        });
    });
    

        const state = store.getState().login;
        expect(state).toEqual({
            id: 1,
            name: 'John Doe',
            email: 'john.doe@example.com',
            isLoggedIn: true,
            role: 'admin',
            token: 'some-token',
        });
    });

    test('should handle logout', () => {
        store.dispatch(logout());
    
        const state = store.getState().login;
        expect(state).toEqual({
            login: {
                id: null,
                name: null,
                email: null,
                isLoggedIn: false,
                role: null,
                token: null
            }
        });
    });
    
        store.dispatch(logout());

        const state = store.getState().login;
        expect(state).toEqual({
            id: null,
            name: null,
            email: null,
            isLoggedIn: false,
            role: null,
            token: null,
        });
    
