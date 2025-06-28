import './index.css';
import App from './App.jsx';
import storeOject from './store.js'
import ReactDOM from 'react-dom/client';
import { Provider } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';


ReactDOM.createRoot(document.getElementById('root')).render(
    <Provider store={storeOject.store}>
      <PersistGate loading={null} persistor={storeOject.persistor}>
        <App />
      </PersistGate>
    </Provider>
)
