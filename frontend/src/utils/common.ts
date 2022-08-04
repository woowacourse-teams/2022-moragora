import { ArrayElement } from 'types/utilityType';

export const mergeArrays = <T extends any[]>(
  prevArray: T,
  newArray: T,
  compareProperty: keyof ArrayElement<T>
) => {
  const updatedArray = prevArray.map((prevElement) => {
    const matchedElementIndex = newArray.findIndex(
      (newEvent) => newEvent[compareProperty] === prevElement[compareProperty]
    );
    const isDuplicated = matchedElementIndex > -1;

    return isDuplicated
      ? newArray.splice(matchedElementIndex, 1)[0]
      : prevElement;
  });

  return [...updatedArray, ...newArray];
};
