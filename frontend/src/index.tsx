import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import { ThemeProvider } from '@emotion/react';
import App from './App';
import { theme } from 'styles/themes/theme';
import GlobalStyles from 'styles/GlobalStyles';
import { UserContextProvider } from 'contexts/userContext';

// if (process.env.NODE_ENV === 'development') {
//   const { worker } = require('./mocks/browser');
//   worker.start();
// }

ReactDOM.render(
  <BrowserRouter>
    <UserContextProvider>
      <GlobalStyles />
      <ThemeProvider theme={theme}>
        <App />
      </ThemeProvider>
    </UserContextProvider>
  </BrowserRouter>,
  document.getElementById('root')
);
