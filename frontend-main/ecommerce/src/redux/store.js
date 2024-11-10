import { configureStore } from "@reduxjs/toolkit";
import storage from "redux-persist/lib/storage"; // defaults to localStorage for web
import { persistReducer, persistStore } from "redux-persist";
import loginReducer from "./userSlice";
import { combineReducers } from "redux";

// Configure persist settings
const persistConfig = {
  key: "root",
  storage,
};

// Combine reducers if you have more than one, otherwise directly use loginReducer in persistReducer
const rootReducer = combineReducers({
  login: loginReducer,
});

// Apply persistReducer to rootReducer
const persistedReducer = persistReducer(persistConfig, rootReducer);

// Create the store with the persisted reducer
const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false, // To ignore non-serializable warning caused by redux-persist
    }),
});

// Create a persistor for the store
export const persistor = persistStore(store);

export default store;
