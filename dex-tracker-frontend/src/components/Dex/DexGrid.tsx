import {
  Box,
  Divider,
  Input,
  InputAdornment,
  Typography,
} from "@mui/material";
import React, { useCallback, useEffect, useState } from "react";
import { Pokemon } from "../../types/user";
import GridColumn from "../Column";
import GridRow from "../Row";
import classNames from "classnames";
import useStyles from "./styles";
import { Search } from "@mui/icons-material";
import { FixedSizeList as List } from "react-window";
import Row from "./Row";

type DexGridProps = {
  pokemon: Pokemon[];
  desktopHeight?: number;
  mobileHeight?: number;
  handleChange: (caught: boolean, index: number) => void;
  gen: number;
};

const DexGrid = (props: DexGridProps) => {
  const classes = useStyles();

  useEffect(() => {
    setItems(props.pokemon);
  }, [props.pokemon]);

  const [search, setSearch] = useState<string | undefined>(undefined);

  const shouldRender = useCallback(
    (p: Pokemon) =>
      search == undefined ||
      search == "" ||
      p.name.toLowerCase().includes(search.toLowerCase()) ||
      p.dexNumber.toString().includes(search),
    [search]
  );

  const handleSearchChange = useCallback(
    (event: React.ChangeEvent<HTMLInputElement>) =>
      setSearch(event.currentTarget.value),
    []
  );

  const togglePokemonCaught = (index: number) => {
    const item = items[index - 1]; // -1 because we are indexing by pokedex id, which starts at 1
    const newItems = items.concat();
    newItems[index - 1] = {
      ...item,
      caught: !item.caught,
    };

    setItems(newItems);
    props.handleChange(!item.caught, index);
  };
  const [items, setItems] = useState(props.pokemon);
  const itemData = {
    items,
    togglePokemonCaught,
    displayedItems: items.filter(shouldRender),
    gen: props.gen,
  };

  return (
    <>
      <GridRow style={{ height: "72px" }}>
        <Box sx={{ display: { xs: 'none', md: 'block' } }}>
          <GridColumn xs={3} md={1} className="center" />
        </Box>
        <Box sx={{ display: { xs: 'none', md: 'block' } }}>
          <GridColumn
            md={1}
            className={classNames("center", "bold", classes.listItem)}
          >
            <Typography variant="button" style={{ fontSize: "14px" }}>
              Number
            </Typography>
          </GridColumn>
        </Box>
        <GridColumn
          xs={9}
          md={8}
          className={classNames(
            "center-v",
            "bold",
            "pl-3",
            "pl-md-0",
            classes.listItem
          )}
        >
          <Input
            fullWidth
            placeholder="Bulbasaur"
            value={search}
            onChange={handleSearchChange}
            endAdornment={
              <InputAdornment position="end">
                <Search />
              </InputAdornment>
            }
          />
        </GridColumn>
        <GridColumn
          xs={3}
          md={1}
          className={classNames("center", "bold", classes.listItem)}
        >
          <Typography variant="button" style={{ fontSize: "14px" }}>
            Caught
          </Typography>
        </GridColumn>
      </GridRow>
      <Divider />
      <Box sx={{ display: { xs: 'none', md: 'block' } }}>
        <List
          height={props.desktopHeight || 720}
          itemCount={items.filter(shouldRender).length}
          itemData={itemData}
          itemSize={72}
          width={"100%"}
        >
          {Row}
        </List>
      </Box>
      <Box sx={{ display: { xs: 'block', md: 'none' } }}>
        <List
          height={props.mobileHeight || 320}
          itemCount={items.filter(shouldRender).length}
          itemData={itemData}
          itemSize={72}
          width={"100%"}
        >
          {Row}
        </List>
      </Box>
    </>
  );
};

export default DexGrid;
